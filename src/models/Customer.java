package models;

public class Customer {
    private int customerId;
    private String fullName;
    private String phone;
    private String apartmentNumber;
    private String buildingName;
    
    public Customer() {}
    
    public int getCustomerId() {
        return customerId; 
    }
    public void setCustomerId(int customerId) { 
        this.customerId = customerId; 
    }
    
    public String getFullName() { 
        return fullName; 
    }
    public void setFullName(String fullName) {
        this.fullName = fullName; 
    }
    
    public String getPhone() {
        return phone; 
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getApartmentNumber() {
        return apartmentNumber; 
    }
    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
    
    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    
    // ⬇️⬇️⬇️ هذا مهم جداً لعرض الاسم في القائمة
    @Override
    public String toString() {
        return fullName + " (شقة " + apartmentNumber + ")";
    }
}
