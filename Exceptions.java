class NoMobilePhone extends Exception
{
    private String message = null;

    public NoMobilePhone() {
        super();
    }

    public NoMobilePhone(String message) {
        super(message);
        this.message = message;
    }

    public NoMobilePhone(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
class NoExchange extends Exception
{
    private String message = null;

    public NoExchange() {
        super();
    }

    public NoExchange(String message) {
        super(message);
        this.message = message;
    }

    public NoExchange(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
class NotRegistered extends Exception
{
    private String message = null;

    public NotRegistered() {
        super();
    }

    public NotRegistered(String message) {
        super(message);
        this.message = message;
    }

    public NotRegistered(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
class AlreadyRegistered extends Exception
{
    private String message = null;

    public AlreadyRegistered() {
        super();
    }

    public AlreadyRegistered(String message) {
        super(message);
        this.message = message;
    }

    public AlreadyRegistered(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
class IndexOutOfRange extends Exception
{
    private String message = null;

    public IndexOutOfRange() {
        super();
    }

    public IndexOutOfRange(String message) {
        super(message);
        this.message = message;
    }

    public IndexOutOfRange(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
class AlreadyOff extends Exception
{
    private String message = null;

    public AlreadyOff() {
        super();
    }

    public AlreadyOff(String message) {
        super(message);
        this.message = message;
    }

    public AlreadyOff(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
class AlreadyOn extends Exception
{
    private String message = null;

    public AlreadyOn() {
        super();
    }

    public AlreadyOn(String message) {
        super(message);
        this.message = message;
    }

    public AlreadyOn(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
