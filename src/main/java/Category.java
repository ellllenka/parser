/**
 * Created by lena on 30.06.16.
 */
public class Category {
    String name1;
    String name2;
    Integer zeroGames;
    Integer zeroFirstTime;

    public Category(String name1, String name2, Integer zeroFirstTime, Integer zeroGames) {
        this.name1 = name1;
        this.name2 = name2;
        this.zeroFirstTime = zeroFirstTime;
        this.zeroGames = zeroGames;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public Integer getZeroGames() {
        return zeroGames;
    }

    public void setZeroGames(Integer zeroGames) {
        this.zeroGames = zeroGames;
    }

    public Integer getZeroFirstTime() {
        return zeroFirstTime;
    }
}
