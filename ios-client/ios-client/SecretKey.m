//
//  SecretKey.m
//  Keysmith IOS Client
//
//  Created by Hakan D on 04/05/2013.
//  Copyright (c) 2013 Hakan Dilek. All rights reserved.

#import "SecretKey.h"

@implementation SecretKey

@synthesize keyData;

- (id)init: (NSData*) data {
    self = [super init];
    if (self) {
        keyData = [data retain];
    }
    return self;
}

- (void)dealloc {
    [keyData release];
    [super dealloc];
}

@end
