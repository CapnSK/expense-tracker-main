// jest.config.js
module.exports = {
    moduleFileExtensions: ['js', 'json', 'vue'],
    transform: {
      '^.+\\.vue$': 'vue-jest',
      '^.+\\.js$': 'babel-jest'
    },
    moduleNameMapper: {
      '^@/(.*)$': '<rootDir>/src/$1'
    },
    testEnvironment: 'jsdom',
    transformIgnorePatterns: [
      '/node_modules/(?!axios)' // axios is ESModule, force it to be transformed
    ]
  };
  