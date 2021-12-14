package org.swasth.dp.claims.functions

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.slf4j.LoggerFactory
import org.swasth.dp.claims.task.ClaimsConfig
import org.swasth.dp.core.function.{BaseDispatcherFunction, ValidationResult}
import org.swasth.dp.core.job.Metrics
import org.swasth.dp.core.util.{JSONUtil, PostgresConnect, PostgresConnectionConfig}

import java.util

class ClaimsProcessFunction(config: ClaimsConfig, @transient var postgresConnect: PostgresConnect = null)(implicit val stringTypeInfo: TypeInformation[String])
  extends BaseDispatcherFunction(config) {

  private[this] val logger = LoggerFactory.getLogger(classOf[ClaimsProcessFunction])

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    if (postgresConnect == null) {
      postgresConnect = new PostgresConnect(PostgresConnectionConfig(
        user = config.postgresUser,
        password = config.postgresPassword,
        database = config.postgresDb,
        host = config.postgresHost,
        port = config.postgresPort,
        maxConnections = config.postgresMaxConnections
      ))
    }
  }

  override def close(): Unit = {
    super.close()
    postgresConnect.closeConnection()
  }

  override def validate(event: util.Map[String, AnyRef]): ValidationResult = {
    // TODO: Add domain specific validations
    Console.println("Triggered validation for event: " + event)
    logger.info("Triggered validation for event: " + event)
    ValidationResult(true, None)
  }

  override def getPayload(event: util.Map[String, AnyRef]): util.Map[String, AnyRef] = {
    val payloadRefId = getPayloadRefId(event)
    Console.println("Fetching payload from postgres for mid: " + payloadRefId)
    logger.info("Fetching payload from postgres for mid: " + payloadRefId)
    val postgresQuery = String.format("SELECT data FROM %s WHERE mid = '%s'", config.payloadTable, payloadRefId);
    val preparedStatement = postgresConnect.getConnection.prepareStatement(postgresQuery)
    try {
      val resultSet = preparedStatement.executeQuery()
      if(resultSet.next()) {
        val payload = resultSet.getString(1)
        Console.println(s"Data from postgres: $payload")
        logger.info(s"Data from postgres: $payload")
        JSONUtil.deserialize[util.Map[String, AnyRef]](payload)
      } else {
        throw new Exception("Payload not found for the given reference id: " + payloadRefId)
      }
    } catch {
      case ex: Exception => throw ex
    } finally {
      preparedStatement.close()
    }

  }

  override def audit(event: util.Map[String, AnyRef], status: Boolean, context: ProcessFunction[util.Map[String, AnyRef], util.Map[String, AnyRef]]#Context, metrics: Metrics): Unit = {
    // TODO: Implement audit here
    val audit = new util.HashMap[String, AnyRef]();
    audit.put("audit", "yes")
    audit.put("status", status.toString)
    context.output(config.auditOutputTag, JSONUtil.serialize(audit))
    metrics.incCounter(config.auditEventsCount)
  }
}
