import java.util.*;
public class RoutingMapTree
{
    Exchange root;

    public RoutingMapTree()
    {
        root = new Exchange(0);
        root.setRoot();
    }

    public RoutingMapTree(Exchange root)
    {
        this.root = root;
    }

    public boolean containsNode(Exchange a) throws IndexOutOfRange
    {
        if(root == a)
        {
            return true;
        }
        for(int i=0; i<root.numChildren();i++)
        {
            if(root.subtree(i+1).containsNode(a))
            {
                return true;
            }
        }
        return false;
    }

    public void switchOn (MobilePhone a, Exchange b) throws AlreadyOn,AlreadyRegistered
    {
        if(a.status())
        {
            //Exception?
            System.out.println("Mobile is already On");
            return;
        }
        else
        {
            a.switchOn();
            a.setBase(b);
            b.registerMobile(a);
            while(!b.isRoot())
            {
                // System.out.println(b.getId());
                b = b.parent();
                // System.out.println(b);
                b.registerMobile(a);
            }
        }
    }

    public void switchOff (MobilePhone a) throws NotRegistered,AlreadyOff
    {
        if(!a.status())
        {
            throw new AlreadyOff("Error - Mobile is already off.");
        }
        else
        {
            Exchange base = a.location();
            a.switchOff();
            while(base != null)
            {
                base.deregisterMobile(a);
                base = base.parent();
            }
        }
    }

    public Exchange SearchExchange(int identifier) throws IndexOutOfRange
    {
        // System.out.println("Root - "+root.getId()+", identifier - "+identifier);
        Exchange temp ;
        if (root.getId() == identifier)
        {
            return root;
        }
        for(int i=0; i<root.numChildren();i++)
        {
            temp = root.subtree(i).SearchExchange(identifier);
            if(temp != null)
            {
                return temp;
            }
        }
        return null;
    }

    public Exchange findPhone (MobilePhone m) throws NotRegistered
    {

        if(root.residentSet().IsMember(m))
        {
            if(m.status())
            {
                return m.location();
            }
            else
            {
                throw new NotRegistered("Error - Mobile Phone Off");
            }
        }
        else
        {
            throw new NotRegistered("Error - No Such MobilePhone");
        }
    }

    public Exchange lowestRouter(Exchange a, Exchange b) throws NoExchange
    {
        if(a==null || b==null)
        {
            throw new NoExchange("Error - No common parent");
        }
        if(a == b)
        {
            return a;
        }
        return lowestRouter(a.parent(),b.parent());
    }

    public ExchangeList routeCall(MobilePhone a, MobilePhone b) throws NotRegistered, NoExchange
    {
        Exchange A = findPhone(a);
        Exchange B = findPhone(b);
        Exchange parent = lowestRouter(A,B);
        ExchangeList path = new ExchangeList();
        tracePath(A,B,parent,path);
        return path;
    }

    protected void tracePath(Exchange a, Exchange b, Exchange parent, ExchangeList path) throws NoExchange, NotRegistered
    {
        path.Add(a);
        if(a == parent)
        {
            return;
        }
        tracePath(a.parent(),b.parent(),parent,path);
        path.Add(b);
    }

    public void movePhone(MobilePhone a, Exchange b) throws NoExchange,NotRegistered,AlreadyOn,AlreadyRegistered,AlreadyOff
    {
        if(b.numChildren() > 0)
        {
            throw new NoExchange("Error - Exchange not Base Station");
        }
        switchOff(a);
        switchOn(a,b);
    }

    public void performAction(String actionMessage)
    {
        Scanner message = new Scanner(actionMessage);
        String action = message.next();
        System.out.println(actionMessage);
        try
        {
            if(action.equals("addExchange"))
            {
                try
                {
                    Exchange exchangeA = SearchExchange(message.nextInt());
                    Exchange exchangeB = new Exchange(message.nextInt());
                    // System.out.println(exchangeA);
                    exchangeA.addChild(exchangeB);
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("switchOnMobile"))
            {
                try
                {
                    int mobileIdentifier = message.nextInt();
                    Exchange exchange = SearchExchange(message.nextInt());
                    if(exchange == null)
                    {
                        throw new NoExchange ("Error - Exchange not present");
                    }
                    else
                    {
                        MobilePhone newMobile = root.searchMobile(mobileIdentifier);
                        if(newMobile != null)
                        {
                            throw new AlreadyOn("Error - Mobile Phone Already On");
                        }
                        newMobile = new MobilePhone(mobileIdentifier);
                        switchOn(newMobile,exchange);
                        // System.out.println("Creation state - "+newMobile.getId()+ " - "+newMobile.status());
                    }
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("switchOffMobile"))
            {
                try
                {
                    MobilePhone mobile = root.searchMobile(message.nextInt());
                    if(mobile == null)
                    {
                        throw new NoMobilePhone("Error - No Such Mobile Phone");
                    }
                    // System.out.println("Search state - "+mobile.getId()+ " - "+mobile.status());
                    switchOff(mobile);
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("queryNthChild"))
            {
                try
                {
                    Exchange exchange = SearchExchange(message.nextInt());
                    int nthChild = message.nextInt();
                    if(exchange == null)
                    {
                        throw new NoExchange("Error - No such Exchange");
                    }
                    System.out.println(exchange.child(nthChild).getId());
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("queryMobilePhoneSet"))
            {
                try
                {
                    String mobileString = "";
                    Exchange exchange = SearchExchange(message.nextInt());
                    if(exchange == null)
                    {
                        throw new NoExchange("Error - No Such Exchange");
                    }
                    // LinkedList mobileSet = exchange.residentSet().objectSet;
                    // LinkedList.Node itr = mobileSet.Head();
                    // if(itr != null)
                    // {
                    //     mobileString = ""+((MobilePhone)itr.data).getId();
                    //     itr = itr.next;
                    // }
                    // while(itr != null)
                    // {
                    //     mobileString = mobileString + ", " + ((MobilePhone)itr.data).getId();
                    //     itr = itr.next;
                    // }
                    String set = exchange.residentSet().toString();
                    System.out.println(set);
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("queryFindPhone"))
            {
                try
                {
                    MobilePhone mobile = root.searchMobile(message.nextInt());
                    System.out.println(findPhone(mobile).getId());
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("queryLowestRouter"))
            {
                try
                {
                    Exchange a = SearchExchange(message.nextInt());
                    Exchange b = SearchExchange(message.nextInt());
                    System.out.println(lowestRouter(a,b));
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("queryFindCallPath"))
            {
                try
                {
                    MobilePhone a = root.searchMobile(message.nextInt());
                    MobilePhone b = root.searchMobile(message.nextInt());
                    System.out.println(routeCall(a,b));
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else if(action.equals("movePhone"))
            {
                try
                {
                    MobilePhone a = root.searchMobile(message.nextInt());
                    Exchange b = SearchExchange(message.nextInt());
                    movePhone(a,b);
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(NoSuchElementException e)
                {
                    System.out.println("Error - Input Format Incorrect");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch(Exception e)
        {
            // System.out.println("HHAHHAHAHAHhaah");
            e.printStackTrace();
        }
        finally
        {
            message.close();
        }
    }
}

class MobilePhone
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

    public String toString()
    {
        return String.valueOf(id);
    }

    public Exchange location() throws NotRegistered
    {
        if(isOn)
        {
            return baseStation;
        }
        else
        {
            throw new NotRegistered("Error - Mobile Phone not registered!");
        }
    }

    public void setBase(Exchange base)
    {
        baseStation = base;
    }
}

class MobilePhoneSet
{
    Myset registeredMobiles;
    public MobilePhoneSet()
    {
        registeredMobiles = new Myset();
    }

    public int size()
    {
        return registeredMobiles.size();
    }

    public boolean IsEmpty()
    {
        return registeredMobiles.IsEmpty();
    }

    public boolean IsMember(MobilePhone o)
    {
        return (registeredMobiles.IsMember(o));
    }

    public void Insert(MobilePhone o) throws AlreadyRegistered
    {
        try
        {
            registeredMobiles.Insert(o);
        }
        catch (Exception e)
        {
            throw new AlreadyRegistered("Error - Mobile Phone Already Registered");
        }
    }
    public void Delete(MobilePhone o) throws NotRegistered
    {
        try
        {
            registeredMobiles.Delete(o);
        }
        catch (Exception e)
        {
            throw new NotRegistered("Error - Mobile Phone Not Registered");
        }
    }

    public MobilePhone Search(int identifier)
    {
        LinkedList masterList = registeredMobiles.objectSet;
        LinkedList.Node itr = masterList.Head();
        while(itr!=null && (((MobilePhone)itr.data).getId() != identifier))
        {
            itr = itr.next;
        }

        if(itr == null)
        {
            return null;
        }
        return (MobilePhone)itr.data;
    }

    public MobilePhoneSet Union(MobilePhoneSet a)
    {
        MobilePhoneSet unionSet = new MobilePhoneSet();
        unionSet.registeredMobiles = this.registeredMobiles.Union(a.registeredMobiles);
        return unionSet;
    }

    public MobilePhoneSet Intersection (MobilePhoneSet a)
    {
        MobilePhoneSet interSet = new MobilePhoneSet();
        interSet.registeredMobiles = this.registeredMobiles.Intersection(a.registeredMobiles);
        return interSet;
    }

    public String toString()
    {
        return registeredMobiles.toString();
    }


}

class Exchange
{
    private int id;
    private boolean isRoot;
    private MobilePhoneSet residentSet;
    private ExchangeList children;
    private Exchange parent;
    public int getId()
    {
        return id;
    }
    public Exchange(int id)
    {
        this.id = id;
        children = new ExchangeList();
        residentSet = new MobilePhoneSet();
    }
    public int numChildren()
    {
        return children.size();
    }

    public void registerMobile(MobilePhone a) throws AlreadyRegistered
    {
        residentSet.Insert(a);
    }

    public void deregisterMobile(MobilePhone a) throws NotRegistered
    {
        residentSet.Delete(a);
    }

    public MobilePhone searchMobile(int identifier)
    {
        return residentSet.Search(identifier);
    }

    public void addChild(Exchange a)
    {
        a.setParent(this);
        // System.out.println(a.getId() + " - "+a.parent().getId());
        children.Add(a);
    }

    public Exchange child(int i) throws IndexOutOfRange
    {
        return children.child(i);
    }
    public Exchange parent()
    {
        return parent;
    }
    public boolean isRoot()
    {
        return isRoot;
    }
    public void setRoot()
    {
        isRoot = true;
    }
    public void removeRoot()
    {
        isRoot = false;
    }
    public MobilePhoneSet residentSet()
    {
        return residentSet;
    }
    public void setParent(Exchange a)
    {
        parent = a;
    }

    public String toString()
    {
        return String.valueOf(id);
    }

    public RoutingMapTree subtree(int i) throws IndexOutOfRange
    {
        Exchange node = child(i);
        if(node != null)
        {
            return new RoutingMapTree(node);
        }
        return null;
    }
}

class ExchangeList
{
    LinkedList list;

    public Exchange child(int i) throws IndexOutOfRange
    {
        if( i < list.size() && i >= 0)
        {
            LinkedList.Node itr = list.Head();
            for(int j=0;j<i;j++)
            {
                itr = itr.next;
            }
            return (Exchange)itr.data;
        }
        else
        {
            throw new IndexOutOfRange("Error - Index Out of Range");
        }
    }

    public ExchangeList()
    {
        list = new LinkedList();
    }

    public int size()
    {
        return list.size;
    }

    public String toString()
    {
        return list.toString();
    }

    public LinkedList.Node Head()
    {
        return list.Head();
    }

    public LinkedList.Node Search(Exchange obj)
    {
        return list.Search(obj);
    }

    public boolean IsEmpty()
    {
        return list.IsEmpty();
    }

    public boolean Add(Exchange data)
    {
        return list.Add(data);
    }

    private LinkedList.Node Remove(LinkedList.Node node)
    {
        return list.Remove(node);
    }

    public void Remove(Exchange o) throws NoExchange
    {
        LinkedList.Node removeNode = Search(o);
        if(removeNode == null)
        {
            throw new NoExchange("Error - No such exchange");
        }
        else
        {
            Remove(removeNode);
        }
    }
}
