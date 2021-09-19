package ru.otus.operations.constants;

public class BusinessProcessConstants {
    public static final String OPEN_OPER_DATE_NAME = "OPEN_OPER_DATE";
    public static final String OPERATIONS_CREATE_SYS_NAME = "OPERATIONS_CREATE";
    public static final String OPERATIONS_CANCEL_SYS_NAME = "OPERATIONS_CANCEL";
    public static final String OPERATIONS_EXECUTION_SYS_NAME = "OPERATIONS_EXECUTION";
    public static final String OPERATIONS_CURRENCY_REVAL_SYS_NAME = "OPERATIONS_CURRENCY_REVAL";
    public static final String CLOSE_OPER_DATE_SYS_NAME = "CLOSE_OPER_DATE";

    public static final int BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSING = Constants.ProtocolStatus.PROCESSING.ordinal();
    public static final int BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED = Constants.ProtocolStatus.PROCESSED.ordinal();

    public static final String BUSINESS_PROCESS_START_INFO = "" + "\n" + "" + "\n" + "" + "\n" + "" + "\n" + "_______________________запуск цепочки обработки бизнес-процессов____________________";
    public static final String BUSINESS_PROCESS_END_BP_INFO = "" + "\n" + "" + "\n" + "" + "\n" + "" + "\n" + "_______________________ВСЕ ПРОЦЕССЫ ЗА ДЕНЬ ВЫПОЛНЕНЫ, МОЖНО ПРИСТУПАТЬ К ЗАКРЫТИЮ ДНЯ____________________";


    private BusinessProcessConstants() {
    }

}
