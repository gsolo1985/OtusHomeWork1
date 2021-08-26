DROP TABLE IF EXISTS OperDate;
DROP TABLE IF EXISTS BusinessProcess;
DROP TABLE IF EXISTS Protocol;
DROP TABLE IF EXISTS Institution;
DROP TABLE IF EXISTS Security;
DROP TABLE IF EXISTS Operation;
DROP TABLE IF EXISTS CurrencyCash;
DROP TABLE IF EXISTS Reval;
DROP TABLE IF EXISTS Document;
DROP TABLE IF EXISTS DocumentTemplate;

CREATE TABLE OperDate (OperDateID BIGINT IDENTITY PRIMARY KEY, OperDate DATE, Status TINYINT);
CREATE TABLE BusinessProcess (BusinessProcessID BIGINT IDENTITY PRIMARY KEY, SysName VARCHAR(60), OrderType INT);
CREATE TABLE Protocol (ProtocolID BIGINT IDENTITY PRIMARY KEY, OperDate DATE, BusinessProcessID BIGINT, Status TINYINT);
CREATE TABLE Institution(InstitutionID BIGINT IDENTITY PRIMARY KEY, Name VARCHAR(255));
CREATE TABLE Security(SecurityID BIGINT IDENTITY PRIMARY KEY, Name VARCHAR(255), Type TINYINT, InstitutionID BIGINT);
CREATE TABLE Operation(OperationID BIGINT IDENTITY PRIMARY KEY, SecurityID BIGINT, OperationDate DATE, PlanDate Date, Num INT, CurrencyID BIGINT, Amount NUMERIC(28,10), State VARCHAR(30), ActualDate DATETIME);
CREATE TABLE CurrencyCash(CurrencyID BIGINT IDENTITY PRIMARY KEY, Name VARCHAR(3));
CREATE TABLE Reval(RevalID BIGINT IDENTITY PRIMARY KEY, OperationID BIGINT, OperDate DATE, RevalValue NUMERIC(28, 10), CurrencyID BIGINT, CurrencyRevalID BIGINT);
CREATE TABLE Document(DocumentID BIGINT IDENTITY PRIMARY KEY, ObjectID BIGINT, OperDate DATE, DebitAccountNumber VARCHAR(20), CreditAccountNumber VARCHAR(20), Amount NUMERIC(28,10), DocComment VARCHAR(255), DocType VARCHAR(30));
CREATE TABLE DocumentTemplate(DocumentTemplateID BIGINT IDENTITY PRIMARY KEY, DocType VARCHAR(30), DocComment VARCHAR(255), OperState VARCHAR(30))