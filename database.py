from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base

engine = create_engine('postgres://eoquaetdmgybuj:6b52d0331a20575e77123f24a6bae730f82447dbcf16b429b2e9674d73c33853@ec2-54-247-189-141.eu-west-1.compute.amazonaws.com:5432/d941vjvjukbn1c', convert_unicode=True)
db_session = scoped_session(sessionmaker(autocommit=False,
                                         autoflush=False,
                                         bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()


def init_db():
    Base.metadata.create_all(bind=engine)