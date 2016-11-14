// Copyright (C) 2016 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.httpd.raw;

import com.google.common.cache.Cache;
import com.google.gerrit.launcher.GerritLauncher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/* Font servlet only used in development mode */
class FontsDevServlet extends ResourceServlet {
  private static final long serialVersionUID = 1L;

  private final Path fonts;

  FontsDevServlet(Cache<Path, Resource> cache, BuildSystem builder)
      throws IOException {
    super(cache, true);
    Objects.requireNonNull(builder);

    BuildSystem.Label zipLabel = builder.fontZipLabel();
    try {
      builder.build(zipLabel);
    } catch (BuildSystem.BuildFailureException e) {
      throw new IOException(e);
    }

    Path zip = builder.targetPath(zipLabel);
    Objects.requireNonNull(zip);

    fonts = GerritLauncher.newZipFileSystem(zip).getPath("/");
  }

  @Override
  protected Path getResourcePath(String pathInfo) throws IOException {
    return fonts.resolve(pathInfo);
  }
}
