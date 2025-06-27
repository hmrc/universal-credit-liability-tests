const randomNino = () => {
    const number = `${Math.floor(Math.random() * 100000)}`.padStart(6, '0');
    return `AA${number}`;
}

const invalidNino =  'AB123456788';

module.exports = {
    randomNino,
    invalidNino
};