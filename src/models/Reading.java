package models;

import java.sql.Date;

public class Reading {
    private int readingId;
    private int customerId;
    private Date readingDate;
    private int previousReading;
    private int currentReading;
    private int consumedUnits;
    private double unitPrice;
    private double totalAmount;
    private String notes;
    
    public Reading() {}
    
    public int getReadingId() { return readingId; }
    public void setReadingId(int readingId) { this.readingId = readingId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public Date getReadingDate() { return readingDate; }
    public void setReadingDate(Date readingDate) { this.readingDate = readingDate; }
    
    public int getPreviousReading() { return previousReading; }
    public void setPreviousReading(int previousReading) { this.previousReading = previousReading; }
    
    public int getCurrentReading() { return currentReading; }
    public void setCurrentReading(int currentReading) { this.currentReading = currentReading; }
    
    public int getConsumedUnits() { return consumedUnits; }
    public void setConsumedUnits(int consumedUnits) { this.consumedUnits = consumedUnits; }
    
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
