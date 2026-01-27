const randomNino = () => {
  const number = `${Math.floor(Math.random() * 100000)}`.padStart(6, '0');
  return `AA${number}`;
}

// TODO: get a random between UC and LCW/LCWRA
const randomUniversalCreditRecordType = 'UC';

// TODO: get a random between Insert and Terminate
const randomUniversalCreditAction = 'Insert';

const invalidNino =  'AB123456788';

module.exports = {
  randomNino,
  invalidNino
};