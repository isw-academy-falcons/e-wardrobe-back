package com.interswitchng.ewardrobe.data.model;

public enum ClothType {
    TOP {
        @Override
        public String toString() {
            return "TOP";
        }
    },
    BOTTOM {
        @Override
        public String toString() {
            return "BOTTOM";
        }
    },
    DRESS {
        @Override
        public String toString() {
            return "DRESS";
        }
    }
}
