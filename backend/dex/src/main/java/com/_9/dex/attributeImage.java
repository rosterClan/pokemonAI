package com._9.dex;

public class attributeImage {
    private int ID; 
    private byte[] base64Image; 

    public attributeImage(int new_id, byte[] base64Image){
        this.ID = new_id;
        this.base64Image = base64Image;
    }
   
    public int get_id() {
        return ID; 
    }

    public byte[] getImage() {
        return base64Image;
    }

    public void set_id(int id) {
        this.ID = id; 
    }

    public void set_image(byte[] image) {
        this.base64Image = image;
    }
}
