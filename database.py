from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from config import SQLALCHEMY_DATABASE_URI
import os

print "Load : " + os.getenv('DATABASE_URL', SQLALCHEMY_DATABASE_URI)

engine = create_engine(os.getenv('DATABASE_URL', SQLALCHEMY_DATABASE_URI), echo = False)

db_session = scoped_session(sessionmaker(autocommit=False,
                                         autoflush=False,
                                         bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()


def init_db():
    import models
    Base.metadata.create_all(bind=engine)
