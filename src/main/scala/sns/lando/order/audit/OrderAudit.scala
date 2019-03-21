package sns.lando.order.audit

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

class OrderAudit {
  def audit(orderId: String, event: OrderEvent.Value): Unit = {
    writeToKafkaAuditTopic(orderId, event)
  }

  private def writeToKafkaAuditTopic(orderId: String, event: OrderEvent.Value): Unit = {
    val orderAuditTopicName = "ORDER_AUDIT"
    val orderAuditTopicProducer = createKafkaProducer

    orderAuditTopicProducer.send(new ProducerRecord(orderAuditTopicName, orderId, event.toString)).get()
  }

  private def createKafkaProducer() = {
    new KafkaProducer[String, String](buildKafkaProperties)
  }

  private def buildKafkaProperties() = {
    val KafkaDeserializer = "org.apache.kafka.common.serialization.StringDeserializer"
    val KafkaSerializer = "org.apache.kafka.common.serialization.StringSerializer"
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put("acks", "all")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaSerializer)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaSerializer)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaDeserializer)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaDeserializer)
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, this.getClass.getName)
    props
  }
}

object OrderEvent extends Enumeration {
  val Received,
  TransformedToJson,
  ParsedSuccessfully,
  EnhancedWithDn,
  EnhancedWithSwitchServiceId,
  KnitwareCommandPrepared = Value
}