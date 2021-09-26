class Main2 {

    public static void main(String[] args)
    {
        String[] stuff = {"kerek","kapocs","kikerte","kegyetlenul","kepviselonk","korat"};
        
        for(String thing : stuff)
        {
            new Thread(() -> System.out.println(thing) ).start();
        }
    }
}
