# Process this file with autoconf to produce a configure script.
AC_INIT([bar],[1.0])
AC_CANONICAL_TARGET([])
AM_INIT_AUTOMAKE

# Minimum Autoconf version required.
AC_PREREQ(2.61)

# Where to generate output; srcdir location.
AC_CONFIG_SRCDIR([bar.c])
AC_CONFIG_HEADERS([config.h])
AC_CONFIG_MACRO_DIR([m4])

# Check for Cygwin specifics
MVNAT_CYGWIN_FLAGS([cygwin])

# Checks for programs.
AC_PROG_CC
AC_ISC_POSIX

AM_PROG_CC_C_O

# Checks for header files.
AC_HEADER_STDC([])
AC_CHECK_HEADERS([stdio.h limits.h string.h dlfcn.h windows.h])

# Checks for libraries.
AC_CHECK_LIB([dl], [dlopen])

AC_CONFIG_FILES([Makefile])
AC_OUTPUT
