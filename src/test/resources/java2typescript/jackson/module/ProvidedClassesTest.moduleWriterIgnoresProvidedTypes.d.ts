export interface NotProvided1 {
}

export interface NotProvidedReferencedOnlyFromProvided1 {
    fieldProvided1: Provided1;
}

export interface NotProvided1ReferencesProvided2 {
    // Provided2 type was excluded when writing out types of the module
    fieldProvided2: Provided2;
}

export interface ClassWithFieldsOfProvidedTypes {
    // Provided1 type was excluded when writing out types of the module
    fieldProvided1: Provided1;
    fieldNotProvided1: NotProvided1;
    notProvided1ReferencesProvided2: NotProvided1ReferencesProvided2;
}

