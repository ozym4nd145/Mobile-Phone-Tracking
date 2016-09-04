public class MobilePhone
{
    private int id;
    private boolean isOn;
    private Exchange baseStation;

    public int getId()
    {
        return id;
    }

    public MobilePhone(int id)
    {
        this.id = id;
    }
    public int number()
    {
        return this.id;
    }
    public boolean status()
    {
        return isOn;
    }
    public void switchOn()
    {
        isOn = true;
    }
    public void switchOff()
    {
        isOn = false;
    }

    public Exchange location()
    {
        if(isOn)
        {
            return baseStation;
        }
        else
        {
            throw new IllegalStateException();
        }
    }

    public void setBase(Exchange base)
    {
        baseStation = base;
    }
}
