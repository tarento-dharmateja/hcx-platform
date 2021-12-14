package org.swasth.dp.claims.task

import com.typesafe.config.Config
import org.swasth.dp.core.job.BaseJobConfig

class ClaimsConfig(override val config: Config) extends BaseJobConfig(config, "ClaimsJob") {

  private val serialVersionUID = 2905979434303791379L

  val postgresUser: String = config.getString("postgres.user")
  val postgresPassword: String = config.getString("postgres.password")
  val postgresTable: String = config.getString("postgres.table")
  val postgresDb: String = config.getString("postgres.database")
  val postgresHost: String = config.getString("postgres.host")
  val postgresPort: Int = config.getInt("postgres.port")
  val postgresMaxConnections: Int = config.getInt("postgres.maxConnections")

  val payloadTable = "payload"

  val kafkaInputTopic = config.getString("kafka.input.topic")

  // Consumers
  val eligibilityCheckConsumer = "claims-consumer"
  val downstreamOperatorsParallelism: Int = config.getInt("task.downstream.operators.parallelism")

}
