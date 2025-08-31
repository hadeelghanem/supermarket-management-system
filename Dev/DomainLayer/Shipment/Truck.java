package DomainLayer.Shipment;

public class Truck {
    private int License_number;
    private String Model;
    private int Id_number;
    private int Net_Weight;
    private int Maximum_Weight;
    private boolean isAvailable;


    public Truck(int license_number, String model, int id_number , int net_Weight ,int maximum_weight) {
        License_number = license_number;
        Model = model;
        Id_number=id_number;
       Net_Weight= net_Weight ;
        Maximum_Weight = maximum_weight;
        isAvailable = true;
    }

    public int getLicense_number() {
        return License_number;
    }

    public String getModel() {
        return Model;
    }

    public int getId_number() {
        return Id_number;
    }

    public int getNet_Weight() {
        return Net_Weight;
    }

    public int getMaximum_weight() {
        return Maximum_Weight;
    }

    public void setLicense_number(int  _license_number) {
        License_number = _license_number;
    }

    public void setModel( String _model) {
        Model = _model;
    }

    public void setMaximum_weight(int _maximum_weight) {
        Maximum_Weight = _maximum_weight;
    }

    public void setId_number(int _Id_number) {
        Id_number = _Id_number;
    }

    public void seNet_Weight(int _Net_Weight) {
        Net_Weight = _Net_Weight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean canCarry(int weight) {
        return weight <= Maximum_Weight;
    }

    public void assignToTransport() {
        isAvailable = false;
    }

    public void releaseFromTransport() {
        isAvailable = true;
    }



}
