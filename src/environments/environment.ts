// const urlPrefix = `${window.location.protocol}//${window.location.hostname}:${window.location.port}`;

const urlPrefix = `http://localhost:8080`;


export const environment = {
  production: false,
  GF_API_ENDPOINT: `${urlPrefix}/api`,
};
