#!/usr/bin/env python
import connexion

app = connexion.App(__name__, specification_dir='./swagger/')
application = app.app
app.add_api('swagger.yaml', arguments={'title': 'This is the API for the Balady Integration Project'})

if __name__ == '__main__':
    app.run(port=8080)
