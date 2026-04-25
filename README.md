This is still a work-in-progress.

## What is this?

The `AXHelper` class installs a set of tools that resolve some known OpenJDK issues across different versions, and/or add some helper features.

Currently it only supports Mac. I'll explore Windows changes before I release v1.0. (Although honestly: our VPAT reviewers found a lot of VoiceOver issues on Mac, and very few NVDA issues on Windows.)

## Disclaimer

In addition to the usual open-source disclaimers, I need to add:

_Most of the interesting work in this project relies on reflection._

This makes it hacky. And while some (many?) will argue using reflection is bad design: my counterargument is, "Sure, but in the meantime this fixes a lot of bugs."

## Context

Most of the fixes offered here also map to specific OpenJDK bugs. (And most of those bugs are bugs I submitted.) I'm also (separately) trying to submit PR's to the OpenJDK client-lib group, so hopefully most of these issues will be resolved in coming months/years.

But in the meantime: our desktop app needs solutions now-ish.
