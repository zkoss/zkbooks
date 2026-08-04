/* TEST FIXTURE - ANTI-PATTERN. NEVER copy this. ${fontFamilyC} is a ZK-5-era
   ThemeProvider execution attribute that nothing populates, so XelNode.interpret writes
   NOTHING and the rule is served as "font-family: ;". The declaration below on the line
   above it is the control: the browser drops only the invalid declaration and keeps the
   rest of the rule. */
.labfix-unresolved-el { color: rgb(1, 2, 3); font-family: ${fontFamilyC}; }
