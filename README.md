# HealthyNeonateParser

## Project Overview

HealthyNeonateParser is a small Java-based ingestion utility for extracting structured drug reference data from NeoFax source documents and loading that data into a SQLite database.

The repository is not a trading, strategy-testing, or live-execution system. Its role is closer to an ETL utility in a medical content workflow: read source documents, normalize semi-structured text, map sections into a fixed schema, and persist the result for downstream use.

## Architecture

The current implementation is intentionally simple and concentrated in a single entry-point class, `Parser`.

- **Document extraction**
  Uses Apache PDFBox to read text from a NeoFax PDF and Apache POI to read text from a NeoFax DOCX file.
- **Text normalization**
  Applies a series of `replaceAll` operations to remove headers/page numbers and insert explicit markers before expected section titles such as `Dose`, `Uses`, `Monitoring`, and `References`.
- **Record parsing**
  Walks the normalized text, locates section boundaries with string indexes, and maps each parsed drug entry into a fixed `String[196][16]` table.
- **Persistence**
  Creates a SQLite table named `info`, then inserts each parsed record through JDBC using a prepared statement.

Although implemented in one class, the effective responsibilities are:

- Input handling: load raw PDF and DOCX content
- Parsing rules: identify field boundaries in NeoFax text
- Data model: hold 16 fields per entry in a fixed in-memory table
- Storage: rebuild and populate a SQLite database

## System Flow

1. The program loads the NeoFax PDF from a hard-coded local path.
2. PDF text is extracted and cleaned to remove recurring document artifacts.
3. Known section names are prefixed with markers so the parser can find boundaries reliably.
4. The parser iterates through title markers and extracts fields such as title, administration, uses, contraindications, pharmacology, monitoring, and references into an in-memory table.
5. The program then loads a NeoFax DOCX file from another hard-coded local path.
6. DOCX text is used as a secondary pass to fill fields that were missed or parsed incorrectly in the PDF pass for specific indexed records.
7. A SQLite database is opened at a hard-coded local path.
8. The `info` table is dropped, recreated, and repopulated with the parsed rows.

## Project Structure

- `src/main/java/Parser.java`  
  The entire application: extraction, normalization, parsing, DOCX fallback handling, and SQLite writes.
- `pom.xml`  
  Maven build file with Java 16 compiler settings and third-party dependencies.
- `README.md`  
  Project documentation.
- `target/classes/Parser.class`  
  Previously compiled output checked into the repository.
- `neofax`  
  Present in the repository but currently empty and unused by the code.

## Technologies Used

- Java 16
- Maven
- Apache PDFBox 2.0.24 for PDF text extraction
- Apache POI 3.17 for DOCX text extraction
- SQLite JDBC 3.15.1 for persistence

## Design Decisions

- **Single-file implementation**
  The current design favors speed of implementation over extensibility. All logic lives in one class, which makes the control flow easy to follow but tightly couples extraction, parsing, and storage.
- **Rule-based parsing**
  Section extraction is driven by explicit string markers and positional indexing. This is appropriate for a fixed source format, but it is brittle if the document layout changes.
- **Two-pass extraction**
  PDF parsing appears to be the primary source, while the DOCX pass is used to repair or supplement specific fields where the PDF text is unreliable.
- **Fixed schema output**
  The parser writes into a known 16-column table, which simplifies downstream querying but assumes the source structure is stable.

## Current Status

Implemented:

- End-to-end parsing from NeoFax source documents into SQLite
- Extraction of major reference sections into a normalized table
- JDBC-based database creation and row insertion

Incomplete or fragile:

- File locations are hard-coded to a specific Windows user directory
- Parsing logic is tailored to a specific NeoFax edition and formatting pattern
- Error handling is minimal; some exceptions are suppressed entirely
- There are no automated tests, configuration files, or command-line options
- The in-memory model is a raw 2D string array rather than a typed domain model
- Some parsing branches are commented out or patched for specific record indexes, indicating manual correction rather than generalized handling

## Future Improvements

- Externalize input and output paths into arguments or configuration
- Split extraction, parsing, and persistence into separate classes
- Replace the fixed string array with a typed record or entity model
- Add validation and structured logging around parse failures
- Introduce tests using representative sample documents
- Make parsing rules data-driven so document changes do not require code edits
- Avoid dropping and recreating the destination table on every run unless explicitly requested
