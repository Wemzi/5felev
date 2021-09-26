package concurent.labs.task;

/**
 * Resources used by ThreadCraft
 *
 * Includes goldmine capacity, gold owned by the player, houses built by the player
 * All the actions manipulating these stats should be implemented here
 */
public class Resources {

    // TODO: Make class thread safe but avoid synchronizing on method level
	//       and try to separate synchronization for updates on number of gold and houses
    // Multiple threads will try to access these resources at the same time

    private Integer goldmineCapacity = Configuration.GOLDMINE_CAPACITY;
    private Integer gold = 0;
    private Integer houses = 0;

    /**
     * If the goldmine hasn't run out yet, mines some gold
     * and adds it to the gold resource
     * @return Whether mining has been successful or not
     */
    public boolean tryToMineGold(){
        synchronized(this.goldmineCapacity)
        {
            if (goldmineCapacity > 0) {
                    synchronized(this.gold)
                    {
                        this.goldmineCapacity -= Configuration.MINING_AMOUNT;
                        this.gold += Configuration.MINING_AMOUNT;
                    }
                return true;
            }
        }

        return false;
    }

    /**
     * Returns number of houses built
     * @return
     */
    public synchronized int getHouses() {
        return houses;
    }

    /**
     * If there is enough gold to build a house, it does
     * Increments number of houses, removes the cost from gold
     * @return Whether building was successful or not
     */
    public boolean tryToBuildHouse() {
        synchronized(this.gold)
        {
            if (this.gold >= Configuration.HOUSE_COST) {

                    synchronized(this.houses)
                    {
                        this.houses++;
                        this.gold -= Configuration.HOUSE_COST;
                    }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns gold owned by the player
     * @return
     */
    public synchronized int getGold(){
        return this.gold;
    }

    /**
     * Return how much gold can be mined from the mine
     * @return
     */
    public synchronized int getGoldmineCapacity(){
        return this.goldmineCapacity;
    }

}
