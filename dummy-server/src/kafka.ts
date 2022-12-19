import { Kafka, ProducerRecord } from 'kafkajs';
import * as logger from 'logger';
import MessageDto from './MessageDto.ts';

const KAFKA_HOST = Deno.env.get('KAFKA_HOST') ?? 'localhost:9092';
const KAFKA_SSL = Deno.env.get('KAFKA_SSL') === 'true' ?? false;
const INCOMING_FROM_SHOWCASE = 'show-case-to-dummy-message';
const OUTGOING_TO_SHOWCASE = 'dummy-to-show-case-message';
const REST_REQUEST = 'localhost:8080/message/from-dummy';

const kafka = new Kafka({
  clientId: 'dummy-server',
  brokers: [KAFKA_HOST],
  ssl: KAFKA_SSL,
});

const producer = kafka.producer();
const consumer = kafka.consumer({ groupId: 'dummy-server' });

const connectKafka = async () => {
  logger.info('Start listen to kafka topics');
  await producer.connect();
  await consumer.connect();
  setupConsumer();
};

const setupConsumer = async () => {
  await consumer.subscribe({
    topic: INCOMING_FROM_SHOWCASE,
    fromBeginning: true,
  });
  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      logger.info(
        `Incoming kafka message on topic ${topic} and partition ${partition}: ${message.value}`,
      );
      if (topic === INCOMING_FROM_SHOWCASE) {
        const msgDto = message.value as MessageDto;
        await handleKafkaMessageFromShowCase(msgDto);
      }
    },
  });
};

const handleKafkaMessageFromShowCase = async (msg: MessageDto) => {
  const sendMessage = {
    content: msg.content,
    messageType: 'REST',
  } satisfies MessageDto;
  const res = await fetch(REST_REQUEST, {
    method: 'POST',
    body: JSON.stringify(sendMessage),
  });
  logger.info(
    `Sent REST request to show-case and got response with status ${res.status}`,
  );
};

const sendKafkaMessageToShowCase = (() => {
  const msgData: ProducerRecord = {
    topic: OUTGOING_TO_SHOWCASE,
    messages: [],
  };
  return async (msg: string) => {
    const message: MessageDto = {
      content: msg,
      messageType: 'KAFKA',
    };
    msgData.messages = [{ value: message }];
    await producer.send(msgData);
  };
})();

const gracefulShutdownKafka = async () => {
  await producer.disconnect();
  await consumer.disconnect();
};

export { connectKafka, gracefulShutdownKafka, sendKafkaMessageToShowCase };
