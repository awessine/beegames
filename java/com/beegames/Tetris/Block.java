package com.beegames.Tetris;


import com.beegames.R;

public class Block {

    private int color;
    private int[][] form;
    private int height;
    private int width;
    private int coordX;
    private int coordY;
    private int rotation;
    private String formName = "";

    public Block() {
        randomForm();
        randomColor();
        randomRotation();
        rotate();
        this.coordY = 0;
        this.coordX = 0;
        this.height = form.length;
        this.width = form[0].length;
    }

    public Block(int coordX, int coordY) {
        randomForm();
        randomColor();
        randomRotation();
        rotate();
        this.coordY = coordY;
        this.coordX = coordX;
        this.height = form.length;
        this.width = form[0].length;
    }


    public void randomRotation() {
        int check = (int) Math.ceil(Math.random() * 4);
        this.rotation = check;
    }

    public void randomColor() {
        int check = (int) Math.ceil(Math.random() * 5);
        if (check == 1) {
            this.color = R.drawable.back_7;
        } else if (check == 2) {
            this.color = R.drawable.back_1;
        } else if (check == 3) {
            this.color = R.drawable.back_2;
        } else if (check == 4) {
            this.color = R.drawable.back_6;
        } else {
            this.color = R.drawable.back_4;
        }
    }

    public void randomForm() {
        int check = (int) Math.ceil(Math.random() * 7);
        if (check == 1) {
            this.formName = "straight";
        } else if (check == 2) {
            this.formName = "square";
        } else if (check == 3) {
            this.formName = "T";
        } else if (check == 4) {
            this.formName = "L";
        } else if (check == 5) {
            this.formName = "duck";
        } else if (check == 6) {
            this.formName = "reversed L";
        } else if (check == 7) {
            this.formName = "reversed duck";
        }
    }

    public void rotate() {
        if (this.formName.equals("straight") && (rotation == 1 || rotation == 3)) {
            this.form = new int[1][4];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 1;
            form[0][3] = 1;
        }
        if (this.formName.equals("straight") && (rotation == 2 || rotation == 4)) {
            this.form = new int[4][1];
            form[0][0] = 1;
            form[1][0] = 1;
            form[2][0] = 1;
            form[3][0] = 1;
        }

        if (this.formName.equals("straight") && (rotation == 1 || rotation == 3)) {
            this.form = new int[1][4];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 1;
            form[0][3] = 1;
        }

        if (this.formName.equals("square")) {
            this.form = new int[2][2];
            form[0][0] = 1;
            form[0][1] = 1;
            form[1][0] = 1;
            form[1][1] = 1;
        }

        if (this.formName.equals("T") && rotation == 1) {
            this.form = new int[2][3];
            form[0][0] = 0;
            form[0][1] = 1;
            form[0][2] = 0;
            form[1][0] = 1;
            form[1][1] = 1;
            form[1][2] = 1;
        }
        if (this.formName.equals("T") && rotation == 2) {
            this.form = new int[3][2];
            form[0][0] = 0;
            form[0][1] = 1;
            form[1][0] = 1;
            form[1][1] = 1;
            form[2][0] = 0;
            form[2][1] = 1;
        }
        if (this.formName.equals("T") && rotation == 3) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 1;
            form[1][0] = 0;
            form[1][1] = 1;
            form[1][2] = 0;
        }
        if (this.formName.equals("T") && rotation == 4) {
            this.form = new int[3][2];
            form[0][0] = 1;
            form[0][1] = 0;
            form[1][0] = 1;
            form[1][1] = 1;
            form[2][0] = 1;
            form[2][1] = 0;
        }

        if (this.formName.equals("L") && rotation == 1) {
            this.form = new int[2][3];
            form[0][0] = 0;
            form[0][1] = 0;
            form[0][2] = 1;
            form[1][0] = 1;
            form[1][1] = 1;
            form[1][2] = 1;
        }
        if (this.formName.equals("L") && rotation == 2) {
            this.form = new int[3][2];
            form[0][0] = 1;
            form[0][1] = 1;
            form[1][0] = 0;
            form[1][1] = 1;
            form[2][0] = 0;
            form[2][1] = 1;
        }
        if (this.formName.equals("L") && rotation == 3) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 1;
            form[1][0] = 1;
            form[1][1] = 0;
            form[1][2] = 0;
        }
        if (this.formName.equals("L") && rotation == 4) {
            this.form = new int[3][2];
            form[0][0] = 1;
            form[0][1] = 0;
            form[1][0] = 1;
            form[1][1] = 0;
            form[2][0] = 1;
            form[2][1] = 1;
        }

        if (this.formName.equals("reversed L") && rotation == 1) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 0;
            form[0][2] = 0;
            form[1][0] = 1;
            form[1][1] = 1;
            form[1][2] = 1;
        }
        if (this.formName.equals("reversed L") && rotation == 2) {
            this.form = new int[3][2];
            form[0][0] = 0;
            form[0][1] = 1;
            form[1][0] = 0;
            form[1][1] = 1;
            form[2][0] = 1;
            form[2][1] = 1;
        }
        if (this.formName.equals("reversed L") && rotation == 3) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 1;
            form[1][0] = 0;
            form[1][1] = 0;
            form[1][2] = 1;
        }
        if (this.formName.equals("reversed L") && rotation == 4) {
            this.form = new int[3][2];
            form[0][0] = 1;
            form[0][1] = 1;
            form[1][0] = 1;
            form[1][1] = 0;
            form[2][0] = 1;
            form[2][1] = 0;
        }

        if (this.formName.equals("duck") && rotation == 1) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 0;
            form[1][0] = 0;
            form[1][1] = 1;
            form[1][2] = 1;
        }
        if (this.formName.equals("duck") && rotation == 2) {
            this.form = new int[3][2];
            form[0][0] = 0;
            form[0][1] = 1;
            form[1][0] = 1;
            form[1][1] = 1;
            form[2][0] = 1;
            form[2][1] = 0;
        }
        if (this.formName.equals("duck") && rotation == 3) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 0;
            form[1][0] = 0;
            form[1][1] = 1;
            form[1][2] = 1;
        }
        if (this.formName.equals("duck") && rotation == 4) {
            this.form = new int[3][2];
            form[0][0] = 0;
            form[0][1] = 1;
            form[1][0] = 1;
            form[1][1] = 1;
            form[2][0] = 1;
            form[2][1] = 0;
        }

        if (this.formName.equals("reversed duck") && rotation == 1) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 0;
            form[1][0] = 0;
            form[1][1] = 1;
            form[1][2] = 1;
        }
        if (this.formName.equals("reversed duck") && rotation == 2) {
            this.form = new int[3][2];
            form[0][0] = 0;
            form[0][1] = 1;
            form[1][0] = 1;
            form[1][1] = 1;
            form[2][0] = 1;
            form[2][1] = 0;
        }
        if (this.formName.equals("reversed duck") && rotation == 3) {
            this.form = new int[2][3];
            form[0][0] = 1;
            form[0][1] = 1;
            form[0][2] = 0;
            form[1][0] = 0;
            form[1][1] = 1;
            form[1][2] = 1;
        }
        if (this.formName.equals("reversed duck") && rotation == 4) {
            this.form = new int[3][2];
            form[0][0] = 0;
            form[0][1] = 1;
            form[1][0] = 1;
            form[1][1] = 1;
            form[2][0] = 1;
            form[2][1] = 0;
        }

    }

    public void changeRotation() {
        this.rotation++;
        if (this.rotation == 5) {
            this.rotation = 1;
        }
        rotate();
        this.width = this.form[0].length;
        this.height = this.form.length;
    }

    public String getFormName() {
        return formName;
    }

    public int getRotation() {
        return rotation;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getColor() {
        return color;
    }

    public int[][] getForm() {
        return form;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setForm(int[][] form) {
        this.form = form;
        this.height = form.length;
        this.width = form[0].length;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
