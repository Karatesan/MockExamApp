FROM node:18.16

WORKDIR /app

COPY entrypoint.sh /entrypoint.sh

ADD . .

RUN npm install

ENTRYPOINT [ "/entrypoint.sh" ]

CMD [ "npm", "run", "dev" ]