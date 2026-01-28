const getRandomElement = (arr) => {
  return arr[Math.floor(Math.random() * arr.length)];
}

const randomNino = () => {
  const number = `${Math.floor(Math.random() * 100000)}`.padStart(6, '0');
  return `AA${number}`;
}

const randomUniversalCreditRecordType = () => {
  return getRandomElement(['UC', 'LCW/LCWRA']);
}

const randomUniversalCreditAction = () => {
  return getRandomElement(['Insert', 'Terminate']);
}

const randomGovUkOriginatorId = () => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  const length = Math.floor(Math.random() * 38) + 3; // 3 to 40 characters long
  return Array.from({ length })
      .map(() => chars[Math.floor(Math.random() * chars.length)])
      .join('');
}

const invalidNino = 'QQ1234567890';

module.exports = {
  randomNino,
  randomGovUkOriginatorId,
  randomUniversalCreditRecordType,
  randomUniversalCreditAction,
  invalidNino
};
