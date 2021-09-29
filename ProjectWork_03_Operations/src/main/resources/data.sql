--Заполнение таблицы с переченью бизнес-процессов:
insert into BusinessProcess(SysName, OrderType, IsOn)
select 'OPEN_OPER_DATE', 1, 1 union all
select 'OPERATIONS_CREATE', 2, 1 union all
select 'OPERATIONS_CANCEL', 3, 1 union all
select 'OPERATIONS_CURRENCY_REVAL', 4, 1 union all
select 'OPERATIONS_EXECUTION', 5, 1 union all
select 'CLOSE_OPER_DATE', 99, 1;

insert into DocumentTemplate (DocType, DocComment, OperState)
select 'DOC_TYPE_CANCEL', 'Документ на отмену по операции с ценной бумагой', 'Отменена' union all
select 'DOC_TYPE_REVAL', 'Валютная переоценка по операции с ценной бумагой', 'Исполнена' union all
select 'DOC_TYPE_LOAD', 'Постановка на учет операции с ценной бумагой', 'Загружена';

--Заполнение таблицы с организациями:
insert into Institution(Name)
select 'Apple inc' union all
select 'Google' union all
select 'Газпром' union all
select 'Роснефть' union all
select 'Samsung' union all
select 'Amazon' union all
select 'IKEA' union all
select 'OZON' union all
select 'Аэрофлот' union all
select 'KIA Motors' union all
select 'LG' union all
select 'MICROSOFT' union all
select 'ORACLE' union all
select 'GITHUB' union all
select 'DIASOFT' union all
select 'OTUS' union all
select 'TELEGRAM' union all
select 'FACEBOOK' union all
select 'Яндекс' union all
select 'Билайн';

--Заполнение таблицы ценных бумаг
insert into Security(Name, Type, InstitutionID)
select 'Акция Apple inc', 1, (select InstitutionID from Institution where Name = 'Apple inc') union all
select 'Облигация Google', 2, (select InstitutionID from Institution where Name = 'Google') union all
select 'Акция Газпром, выпуск 1', 1, (select InstitutionID from Institution where Name = 'Газпром') union all
select 'Акция Газпром, выпуск 2', 1, (select InstitutionID from Institution where Name = 'Газпром') union all
select 'Облигация Роснефть', 2, (select InstitutionID from Institution where Name = 'Роснефть') union all
select 'Облигация Samsung', 2, (select InstitutionID from Institution where Name = 'Samsung') union all
select 'Облигация Amazon', 2, (select InstitutionID from Institution where Name = 'Samsung') union all
select 'Акция IKEA, выпуск 1', 1, (select InstitutionID from Institution where Name = 'IKEA') union all
select 'Акция IKEA, выпуск 2', 1, (select InstitutionID from Institution where Name = 'IKEA') union all
select 'Акция IKEA, выпуск 3', 1, (select InstitutionID from Institution where Name = 'IKEA') union all
select 'Акция IKEA, выпуск 4', 1, (select InstitutionID from Institution where Name = 'IKEA') union all
select 'Облигация OZON', 2, (select InstitutionID from Institution where Name = 'OZON') union all
select 'Акция Аэрофлот, выпуск 1', 1, (select InstitutionID from Institution where Name = 'Аэрофлот') union all
select 'Акция Аэрофлот, выпуск 2', 1, (select InstitutionID from Institution where Name = 'Аэрофлот') union all
select 'Акция Аэрофлот, выпуск 3', 1, (select InstitutionID from Institution where Name = 'Аэрофлот') union all
select 'Акция Аэрофлот, выпуск 4', 1, (select InstitutionID from Institution where Name = 'Аэрофлот') union all
select 'Облигация KIA Motors', 2, (select InstitutionID from Institution where Name = 'KIA Motors') union all
select 'Облигация LG', 2, (select InstitutionID from Institution where Name = 'LG') union all
select 'Акция MICROSOFT, выпуск 1', 1, (select InstitutionID from Institution where Name = 'MICROSOFT') union all
select 'Акция MICROSOFT, выпуск 2', 1, (select InstitutionID from Institution where Name = 'MICROSOFT') union all
select 'Акция MICROSOFT, выпуск 3', 1, (select InstitutionID from Institution where Name = 'MICROSOFT') union all
select 'Акция ORACLE', 1, (select InstitutionID from Institution where Name = 'ORACLE') union all
select 'Облигация GITHUB', 2, (select InstitutionID from Institution where Name = 'GITHUB') union all
select 'Акция DIASOFT, выпуск 1', 1, (select InstitutionID from Institution where Name = 'DIASOFT') union all
select 'Акция DIASOFT, выпуск 2', 1, (select InstitutionID from Institution where Name = 'DIASOFT') union all
select 'Акция OTUS', 1, (select InstitutionID from Institution where Name = 'OTUS') union all
select 'Акция TELEGRAM', 1, (select InstitutionID from Institution where Name = 'TELEGRAM') union all
select 'Акция FACEBOOK, выпуск 1', 1, (select InstitutionID from Institution where Name = 'FACEBOOK') union all
select 'Акция FACEBOOK, выпуск 2', 1, (select InstitutionID from Institution where Name = 'FACEBOOK') union all
select 'Акция FACEBOOK, выпуск 3', 1, (select InstitutionID from Institution where Name = 'FACEBOOK') union all
select 'Акция FACEBOOK, выпуск 4', 1, (select InstitutionID from Institution where Name = 'FACEBOOK') union all
select 'Акция FACEBOOK, выпуск 5', 1, (select InstitutionID from Institution where Name = 'FACEBOOK') union all
select 'Акция FACEBOOK, выпуск 6', 1, (select InstitutionID from Institution where Name = 'FACEBOOK') union all
select 'Акция Яндекс, выпуск 1', 1, (select InstitutionID from Institution where Name = 'Яндекс') union all
select 'Акция Яндекс, выпуск 2', 1, (select InstitutionID from Institution where Name = 'Яндекс') union all
select 'Облигация Билайн', 2, (select InstitutionID from Institution where Name = 'Билайн');


insert into CurrencyCash (name)
select 'RUB' union all
select 'USD' union all
select 'EUR' union all
select 'GBP' union all
select 'CHF' union all
select 'JPY' union all
select 'CNY';


