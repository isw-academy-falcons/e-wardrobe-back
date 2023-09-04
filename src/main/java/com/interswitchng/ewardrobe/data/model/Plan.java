package com.interswitchng.ewardrobe.data.model;

import lombok.ToString;

public enum Plan {
    FREE {
        @Override
        public String toString() {
            return "FREE";
        }
    },
    PREMIUM {
        @Override
        public String toString() {
            return "PREMIUM";
        }
    };
}

