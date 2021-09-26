class Main
{
    public static void main(String[] args)
    {
        new Thread(() -> writeNumbersFrom1To100() ).start();
        new Thread(() -> writeNumbersFromMinus1To100() ).start();
    }

    public static void writeNumbersFrom1To100()
    {
        for(int idx=1; idx<100001;idx++)
        {
            System.out.println(idx);
        }
    }

    public static void writeNumbersFromMinus1To100()
    {
        for(int idx=-1; idx>-100001;idx--)
        {
            System.out.println(idx);
        }
    }
}