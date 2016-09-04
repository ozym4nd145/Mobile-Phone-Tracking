import java.util.*;

class ExchangeList extends LinkedList
{

}

class MobilePhoneSet extends Myset
{

}

public class Exchange
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

    public void registerMobile(MobilePhone a)
    {
        residentSet.Insert(a);
    }

    public void deregisterMobile(MobilePhone a)
    {
        residentSet.Delete(a);
    }

    public void addChild(Exchange a)
    {
        a.setParent(this);
        // System.out.println(a.getId() + " - "+a.parent().getId());
        children.Add(a);
    }

    public Exchange child(int i)
    {
        if( i < children.size() && i >= 0)
        {
            ExchangeList.Node itr = children.Head();
            for(int j=0;j<i;j++)
            {
                itr = itr.next;
            }
            return (Exchange)itr.data;
        }
        else
        {
            return null;
        }
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

    public RoutingMapTree subtree(int i)
    {
        Exchange node = child(i);
        if(node != null)
        {
            return new RoutingMapTree(node);
        }
        return null;
    }
}

class RoutingMapTree
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

    public boolean containsNode(Exchange a)
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

    public void switchOn (MobilePhone a, Exchange b)
    {
        if(a.status())
        {
            //Exception?
            System.out.println("Mobile is Not Off!");
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

    public void switchOff (MobilePhone a)
    {
        if(!a.status())
        {
            System.out.println("Mobile is not on!!");
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

    public MobilePhone SearchMobile(int identifier)
    {
        LinkedList masterList = root.residentSet().objectSet;
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

    public Exchange SearchExchange(int identifier)
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

    public void performAction(String actionMessage)
    {
        Scanner message = new Scanner(actionMessage);
        String action = message.next();
        try
        {
            if(action.equals("addExchange"))
            {
                System.out.println(actionMessage);
                Exchange exchangeA = SearchExchange(message.nextInt());
                Exchange exchangeB = new Exchange(message.nextInt());
                // System.out.println(exchangeA);
                if(exchangeA == null)
                {
                    throw new Exception();
                }
                exchangeA.addChild(exchangeB);
            }
            else if(action.equals("switchOnMobile"))
            {
                System.out.println(actionMessage);
                int mobileIdentifier = message.nextInt();
                Exchange exchange = SearchExchange(message.nextInt());
                if(exchange == null)
                {
                    throw new Exception ();
                }
                else
                {
                    MobilePhone newMobile = SearchMobile(mobileIdentifier);
                    if(newMobile != null)
                    {
                        throw new Exception();
                    }
                    newMobile = new MobilePhone(mobileIdentifier);
                    switchOn(newMobile,exchange);
                    // System.out.println("Creation state - "+newMobile.getId()+ " - "+newMobile.status());
                }
            }
            else if(action.equals("switchOffMobile"))
            {
                System.out.println(actionMessage);
                MobilePhone mobile = SearchMobile(message.nextInt());
                if(mobile == null)
                {
                    throw new Exception ();
                }
                // System.out.println("Search state - "+mobile.getId()+ " - "+mobile.status());
                switchOff(mobile);
            }
            else if(action.equals("queryNthChild"))
            {
                System.out.print(actionMessage+" : ");
                Exchange exchange = SearchExchange(message.nextInt());
                int nthChild = message.nextInt();
                if(exchange == null)
                {
                    throw new Exception();
                }
                System.out.println(exchange.child(nthChild).getId());
            }
            else if(action.equals("queryMobilePhoneSet"))
            {
                System.out.print(actionMessage+" : ");
                String mobileString = "";
                Exchange exchange = SearchExchange(message.nextInt());
                if(exchange == null)
                {
                    throw new Exception();
                }
                LinkedList mobileSet = exchange.residentSet().objectSet;
                LinkedList.Node itr = mobileSet.Head();
                if(itr != null)
                {
                    mobileString = ""+((MobilePhone)itr.data).getId();
                    itr = itr.next;
                }
                while(itr != null)
                {
                    mobileString = mobileString + ", " + ((MobilePhone)itr.data).getId();
                    itr = itr.next;
                }
                System.out.println(mobileString);
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
