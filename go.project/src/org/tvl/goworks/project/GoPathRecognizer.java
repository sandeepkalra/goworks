/*
 * [The "BSD license"]
 *  Copyright (c) 2012 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tvl.goworks.project;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.modules.parsing.spi.indexing.PathRecognizer;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Sam Harwell
 */
@ServiceProvider(service=PathRecognizer.class, position=100)
public class GoPathRecognizer extends PathRecognizer {
    private final Set<String> MIME_TYPES = Collections.singleton("text/x-go");
    private final Set<String> SOURCES = Collections.singleton(GoProject.SOURCE);
    private static final Set<String> BINARIES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {
        ClassPath.BOOT,
        ClassPath.COMPILE
    })));

    @Override
    public Set<String> getSourcePathIds() {
        return SOURCES;
    }

    @Override
    public Set<String> getBinaryLibraryPathIds() {
        return BINARIES;
    }

    @Override
    public Set<String> getLibraryPathIds() {
        return null;
    }

    @Override
    public Set<String> getMimeTypes() {
        return MIME_TYPES;
    }

}
