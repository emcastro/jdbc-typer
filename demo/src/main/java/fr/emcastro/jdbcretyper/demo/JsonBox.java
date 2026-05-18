package fr.emcastro.jdbcretyper.demo;

// Wrapper for a JSON string. The same record exists in the test source tree
// (fr.emcastro.jdbcretyper.JsonBox) — this duplication is intentional:
// the demo is a standalone subproject and must not depend on src/test.
public record JsonBox(String value) {
    
}
