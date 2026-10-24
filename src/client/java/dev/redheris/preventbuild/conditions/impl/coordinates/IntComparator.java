package dev.redheris.preventbuild.conditions.impl.coordinates;

public enum IntComparator {
    GREATER {
        @Override
        public boolean compare(int a, int b) {
            return a > b;
        }
    },
    GREATER_EQ {
        @Override
        public boolean compare(int a, int b) {
            return a >= b;
        }
    },
    LESS {
        @Override
        public boolean compare(int a, int b) {
            return a < b;
        }
    },
    LESS_EQ {
        @Override
        public boolean compare(int a, int b) {
            return a <= b;
        }
    };

    public abstract boolean compare(int a, int b);
}
