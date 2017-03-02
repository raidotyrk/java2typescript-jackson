export interface Subclass {
    // only PUBLIC fields from all class hierarchy levels
    inSubClassPublicField: string;
    inSuperclassPublicField: string;
    inBaseclassPublicField: string;
    // fields declared in multiple classes are emitted only once
    inBaseclassPublicFieldOverriddenInSubclass: string;
    // properties should be generated from public getter & setter pairs (even if there is no corresponding field)
    publicPropertyInSubclass: string;
    publicPropertyInSuperclass: string;
    publicPropertyInBaseclass: string;
    // property generated from public getter `getReadable()`, but not from setter `setWritable(value)`
    publicReadable: string;
}

