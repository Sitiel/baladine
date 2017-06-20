from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
import os

try:
    os.environ['DATABASE_URL']
except NameError:
    print "Load : " + 'postgresql://localhost/balady'
    engine = create_engine('postgresql://localhost/balady', echo = True)
else:
    print "Load : " + os.environ['DATABASE_URL']
    engine = create_engine(os.environ['DATABASE_URL'], echo = True)

db_session = scoped_session(sessionmaker(autocommit=False,
                                         autoflush=False,
                                         bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()


def init_db():
    Base.metadata.create_all(bind=engine)