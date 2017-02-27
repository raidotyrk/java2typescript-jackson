export module mod {

    export class TestClassStatic {
        static MY_CONSTANT_ENUM: ChangedEnumName = ChangedEnumName.VAL1;
        static MY_CONSTANT_STRING: string = 'Test';
        static MY_CONSTANT_ENUM_ARRAY: ChangedEnumName[] = [ ChangedEnumName.VAL1 ];
        static MY_CONSTANT_LONG: number = 100;
        static MY_CONSTANT_LONG_WRAPPER: number = 101;
        static MY_CONSTANT_FLOAT: number = 21.06;
        static MY_CONSTANT_FLOAT_WRAPPER: number = 21.07;
        static MY_CONSTANT_DOUBLE: number = 42.12;
        static MY_CONSTANT_DOUBLE_WRAPPER: number = 42.13;
        static MY_CONSTANT_BIG_INTEGER: number = 1;
        static MY_CONSTANT_BIG_DECIMAL: number = 234.5;
        static MY_CONSTANT_ATOMIC_INTEGER: number = 2;
        static MY_CONSTANT_ARRAY: string[] = [ 'Test' ];
        static MY_CONSTANT_INT_ARRAY: number[] = [ 10, 12 ];
        static MY_CONSTANT_LONG_ARRAY: number[] = [ 1000, 1200 ];
        static MY_CONSTANT_LONG_WRAPPER_ARRAY: number[] = [ 2000, 2200 ];
        static MY_CONSTANT_DOUBLE_ARRAY: number[] = [ 42.12 ];
        static MY_CONSTANT_BOOLEAN_ARRAY: boolean[] = [ true, false, true ];
        static MY_CONSTANT_BOOLEAN: boolean = true;
        static MY_CONSTANT_FLOAT_ARRAY: number[] = [ 121.06, 221.06 ];
        static MY_CONSTANT_FLOAT_WRAPPER_ARRAY: number[] = [ 121.07, 221.07 ];
        static MY_CONSTANT_INT: number = 10;
        static MY_CONSTANT_BIG_INTEGER_ARRAY: number[] = [ 1, 10 ];
        static MY_CONSTANT_BIG_DECIMAL_ARRAY: number[] = [ 234.5, 334.5 ];
        static MY_CONSTANT_ATOMIC_INTEGER_ARRAY: number[] = [ 21, 22 ];
        static MY_CONSTANT_ENUM_ARRAY_2: ChangedEnumName[] = [ ChangedEnumName.VAL1, ChangedEnumName.VAL2 ];
    }

    export enum ChangedEnumName {
        VAL1,
        VAL2,
        VAL3,
    }

}

