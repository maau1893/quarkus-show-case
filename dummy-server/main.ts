import { Application, Router } from 'oak';
import oakLogger from 'oak/logger';
import { Kafka } from 'kafkajs';
import * as logger from 'logger';

const PORT = parseInt(Deno.env.get('PORT') ?? '3000');
const KAFKA_HOST = Deno.env.get('KAFKA_HOST') ?? 'localhost:9092';
const KAFKA_SSL = Deno.env.get('KAFKA_SSL') === 'true' ?? false;

logger.setup({
  handlers: {
    functionFmt: new logger.handlers.ConsoleHandler('INFO', {
      formatter: (logRecord) =>
        `[${logRecord.datetime.toISOString()} ${logRecord.loggerName} ${logRecord.levelName}] ${logRecord.msg}`,
    }),
  },
  loggers: {
    default: {
      level: 'INFO',
      handlers: ['functionFmt'],
    },
  },
});

logger.info(KAFKA_HOST);

const kafka = new Kafka({
  clientId: 'dummy-server',
  brokers: [KAFKA_HOST],
  ssl: KAFKA_SSL,
});

const producer = kafka.producer();
const consumer = kafka.consumer({ groupId: 'dummy-server' });

const connectKafka = async () => {
  logger.info('Start listen to kafka topics');
  producer.connect();
  consumer.connect();
  consumer.subscribe({ topic: 'test-topic', fromBeginning: true });
  await consumer.run({
    // deno-lint-ignore require-await
    eachMessage: async ({ topic, partition, message }) => {
      logger.info(
        `Incoming kafka message on topic ${topic} and partition ${partition}: ${message.value}`,
      );
    },
  });
};

const gracefulShutdownKafka = async () => {
  await producer.disconnect();
  await consumer.disconnect();
};

const router = new Router();
router.get('/', (ctx) => {
  producer.send({ topic: 'test-topic', messages: [{ value: 'Hello world!' }] });
  ctx.response.body = 'Hello world!';
});

const app = new Application();
app.use(oakLogger.logger);
app.use(oakLogger.responseTime);
app.use(router.routes());
app.use(router.allowedMethods());

const gracefulShutdown = async () => {
  logger.info('Shutdown signal received');
  await gracefulShutdownKafka();
  Deno.exit();
};

await connectKafka();

Deno.addSignalListener('SIGINT', gracefulShutdown);

logger.info(`App listening on http://localhost:${PORT}`);
await app.listen({ port: PORT });
