package DomainLayer.Shipment;

public class Product{
   private  int Weight;
   private  String Type;
   private int quantity;


  public Product(int weight, String type, int quantity) {
       Weight = weight;
       Type = type;
       this.quantity = quantity;
   }

    public Product() {

    }

    public int getWeight() {
       return Weight;
   }

   public String getType() {
       return Type;
   }

   public int getQuantity() {
       return quantity;
   }

   public void setQuantity(int quantity) {
       this.quantity = quantity;
   }


    public void setType(String group) {
        this.Type = group;
    }

    public void setWeight(int parseint) {
        this.Weight = parseint;
    }
}
