const getRandomElement = (arr) => {
  return arr[Math.floor(Math.random() * arr.length)];
}

const randomNino = () => {
  const number = `${Math.floor(Math.random() * 1000000)}`.padStart(6, '0');
  return `AA${number}`;
}

const randomInvalidNino = () => {
  const number = `${Math.floor(Math.random() * 100000000)}`.padStart(8, '0');
  return `ZZ${number}`;
}

const randomGovUkOriginatorId = () => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  const length = Math.floor(Math.random() * 38) + 3; // 3 to 40 characters long
  return Array.from({ length })
      .map(() => chars[Math.floor(Math.random() * chars.length)])
      .join('');
}

const randomUniversalCreditRecordType = () => {
  return getRandomElement(['UC', 'LCW/LCWRA']);
}

const randomUniversalCreditAction = () => {
  return getRandomElement(['Insert', 'Terminate']);
}

module.exports = {
  randomNino,
  randomInvalidNino,
  randomGovUkOriginatorId,
  randomUniversalCreditRecordType,
  randomUniversalCreditAction
};
