import { Application } from './deps.ts';
import { oakLogger } from './deps.ts';
import { logger } from './deps.ts';
import router from './src/routes.ts';
import { connectKafka, gracefulShutdownKafka } from './src/kafka.ts';

const PORT = parseInt(Deno.env.get('PORT') ?? '3000');

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
