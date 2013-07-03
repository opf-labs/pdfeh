xslt outputs
============

This directory contains outputs resulting from running apache preflight and then parsing the outputs through an XSLT stylesheet that defines a basic policy. 

A policy defines under what conditions a PDF/A is marked as being valid. So although it might not be strictly valid according to preflight, the policy may say that the identified error is to be ignored, thus the PDF is marked as being valid.

A critical part of the process is thus to record which policy was applied during the validation, otherwise it is very unclear which of the following 3 situations applies.

1. PDF is valid
2. PDF is invalid but local policy overrides the errors
3. PDF is invalid and local policy does not override the errors
