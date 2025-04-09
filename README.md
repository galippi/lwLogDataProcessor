Simple Java utility to process text based stream received on channel.
It calls the configured callback, if a known data is received.
It calls the configured default callback, if an unknown data is received.
e.g.:
U01234 -> prefix U0, received data 1234
DBG0ABCDEF00 -> prefix DBG0, received data ABCDEF00
ABCDED -> not configured prefix: forwards the all ABCDED
