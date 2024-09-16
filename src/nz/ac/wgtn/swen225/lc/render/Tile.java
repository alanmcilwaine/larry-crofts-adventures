public class Tile <T extends Item>{
    Item item;
    String getItemOnTile(){
        return item.getClass().getSimpleName();
    }

    public Location location;
}
