type MessageDto = {
  content: string;
  messageType: 'REST' | 'KAFKA';
};

export default MessageDto;
