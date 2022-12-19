import { Router } from 'oak';
import * as logger from 'logger';
import { sendKafkaMessageToShowCase } from './kafka.ts';
import MessageDto from './MessageDto.ts';

const router = new Router();
router.get('/', (ctx) => {
  ctx.response.body = 'Hello world!';
}).post('/message/from-show-case', async (ctx) => {
  const requestBody = await ctx.request.body().value as MessageDto;
  logger.info('Send kafka message');
  sendKafkaMessageToShowCase(requestBody.content);
});

export default router;
