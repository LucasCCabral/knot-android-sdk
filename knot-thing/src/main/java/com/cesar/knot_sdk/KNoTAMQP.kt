package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import java.io.IOException


/**
 * This class is an abstraction over the rabbitMQ java library it handles all
 * AMQP specific operations, such as creation of connection, queues and
 * consumers.
 * This class contains all KNoT-Protocol specific keywords, such as, the binding
 * keys and queue/consumer names.
 * @param username the rabbitMQ username in the connector
 * @param password the rabbitMQ password in the connector
 * @param hostname the ip of the server running the connector
 * @param port the port that rabbitMQ will be listening to (by default 5672)
 */
class KNoTAMQP(username : String, password : String, hostname : String, port : Int) {

    val EXCHANGE_TYPE_TOPIC = "topic"

    val EXCHANGE_NAME_CLOUD = "cloud"
    val EXCHANGE_NAME_FOG = "fog"

    val BINDING_KEY_REGISTER = "device.register"
    val BINDING_KEY_REGISTERED = "device.registered"
    val BINDING_KEY_UNREGISTER = "device.unregister"
    val BINDING_KEY_AUTHENTICATE = "device.cmd.auth"
    val BINDING_KEY_SCHEMA_UPDATE = "schema.update"
    val BINDING_KEY_DATA_PUBLISH = "data.publish"
    val BINDING_KEY_DATA_UPDATE = "data.update"

    val QUEUE_NAME_FOG_IN = "fogIn"
    val QUEUE_NAME_FOG_OUT = "fogOut"

    val CONSUMER_NAME = "KNoTThingConsumer"

    val connectionFactory = ConnectionFactory()

    lateinit var conn : Connection
    lateinit var channel : Channel

    init {
        connectionFactory.host = hostname
        connectionFactory.username = username
        connectionFactory.password = password
        connectionFactory.port = port
    }

    /**
     * Starts the connection with the rabbitMQ server. Uses the parameters
     * defined in factory. Should not be executed in the main thread as it
     * performs network operations.
     */
    fun startConnection() {
        conn = connectionFactory.newConnection()
        channel = conn.createChannel()
    }

    /**
     * Creates an durable exchange with the name and type given in the
     * parametes.
     * @param exchangeName the name of the exchange
     * @param exchangeType the type of the exchange
     */
    fun createExchange(
        exchangeName : String,
        exchangeType : String = EXCHANGE_TYPE_TOPIC) {

        val exchangeException = "Something went wrong while creating this " +
                "exchange: $exchangeName. Please try again, later."

        val res = channel.exchangeDeclare(
            exchangeName,
            exchangeType,
            true
        )
        if (res !is AMQP.Exchange.DeclareOk) throw IOException(exchangeException)
    }

    /**
     * Creates a durable, non-exclusive and non self-deleting queue without
     * extra parameters with the given name.
     * @param queueName the name of the queue, must be either fogIn or fogOut.
     */
    fun createQueue(queueName: String) {
        val durable = true
        val exclusive = false
        val autoDelete = false
        val arguments = null
        channel.queueDeclare(
            queueName,
            durable,
            exclusive,
            autoDelete,
            arguments
        ).queue
    }

    /**
     * Binds a queue to an exchange and a routing key. Both the queue and the
     * exchange should already exist in the channel.
     * @param queueName name of an existing queue
     * @param exchangeName name of an existing exchange
     * @param routingKey the name of the desired queue
     */
    fun bindQueue( queueName : String, exchangeName : String, routingKey: String) {
        val queueException = "Something went wrong while binding this " +
                "queue: $exchangeName. Please try again, later."
        val res = channel.queueBind(queueName, exchangeName, routingKey)
        if (res !is AMQP.Queue.BindOk) throw IOException(queueException)
    }

    /**
     * Publishes a message, in the specified exchange with the specified routing
     * key. The message being sent, should respect the KNoT Protocol, which
     * means it should be a formatted JSON and the routing key and exchange
     * should be the proper ones.
     * @param message the message that is going to be sent
     * @param exchange the exchange that will hold the message
     * @param routingKey the routing key the message is associated with
     */
    fun publish(message : String, exchange : String, routingKey : String) {
        val messageBodyBytes = message.toByteArray()
        val messageProperties = AMQP.BasicProperties.Builder()
            .deliveryMode(2)
            .build()

        channel.basicPublish(
            exchange,
            routingKey,
            messageProperties,
            messageBodyBytes
        )
    }

    /**
     * Creates a consumer that listens to an AMQP queue. This is used to listen to the messages
     * being sent by the cloud to the Thing.
     * @param queueName the name of the queue that the Thing will listen to
     * @param consumerTag a name for this consumer, the purpose of this parameter is to
     * destroy or release the consumer
     */
    fun createConsumer(queueName : String, consumerTag : String) {
        val autoAck = false
        channel.basicConsume(queueName, autoAck, consumerTag,
            object : DefaultConsumer(channel) {
                @Throws(IOException::class)
                override fun handleDelivery(
                    consumerTag: String,
                    envelope: Envelope,
                    properties: AMQP.BasicProperties,
                    body: ByteArray
                ) {
                    val message = String(body)
                    when (envelope.routingKey) {
                        BINDING_KEY_REGISTERED    -> KNoTStateMachine.registerMessageReceived(message)
                        BINDING_KEY_UNREGISTER    -> KNoTStateMachine.unregisterMessageReceived(message)
                        BINDING_KEY_AUTHENTICATE  -> KNoTStateMachine.authMessageReceived(message)
                        BINDING_KEY_SCHEMA_UPDATE -> KNoTStateMachine.schemaUpdateMessageReceived(message)
                        BINDING_KEY_DATA_PUBLISH  -> KNoTStateMachine.dataRequestMessageReceived(message)
                        BINDING_KEY_DATA_UPDATE   -> KNoTStateMachine.dataUpdateMessageReceived(message)
                        else                      -> LogWrapper.log(
                                "The received message does not belonged to a valid binding key"
                            )
                    }
                }
            })
    }

    /**
     * This method is responsible for releasing the resources used by the AMQP
     * communication. This method should only be called when ending a connection
     * (when there is no intention of publishing/consuming data). Ideally this
     * method should only be called when the application is being destroyed.
     */
    fun disconnect() {
            channel.close()
            conn.close()
    }
}
