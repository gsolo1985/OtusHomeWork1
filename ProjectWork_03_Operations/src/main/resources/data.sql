insert into BusinessProcess(SysName, OrderType)
select 'CHECK_START_DAY_DATA', 0 union all
select 'OPEN_OPER_DATE', 1 union all
select 'OPERATIONS_CREATE', 2 union all
select 'OPERATIONS_EXECUTION', 3 union all
select 'OPERATIONS_CANCEL', 4 union all
select 'OPERATIONS_CURRENCY_REVAL', 5 union all
select 'CLOSE_OPER_DATE', 99;
