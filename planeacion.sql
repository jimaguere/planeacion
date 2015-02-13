--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.15
-- Dumped by pg_dump version 9.1.15
-- Started on 2015-02-12 12:34:25 COT

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 166 (class 3079 OID 11645)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1941 (class 0 OID 0)
-- Dependencies: 166
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 167 (class 3079 OID 246982)
-- Dependencies: 5
-- Name: pg_similarity; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pg_similarity WITH SCHEMA public;


--
-- TOC entry 1942 (class 0 OID 0)
-- Dependencies: 167
-- Name: EXTENSION pg_similarity; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_similarity IS 'support similarity queries';


SET search_path = public, pg_catalog;

--
-- TOC entry 1471 (class 3600 OID 247058)
-- Dependencies: 5
-- Name: simple_dict; Type: TEXT SEARCH DICTIONARY; Schema: public; Owner: postgres
--

CREATE TEXT SEARCH DICTIONARY simple_dict (
    TEMPLATE = pg_catalog.simple,
    stopwords = 'spanish', accept = 'false' );


ALTER TEXT SEARCH DICTIONARY public.simple_dict OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 164 (class 1259 OID 246972)
-- Dependencies: 5
-- Name: documento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE documento (
    id integer NOT NULL,
    titulo text,
    autores text,
    ruta text,
    vistos integer,
    fecha date
);


ALTER TABLE public.documento OWNER TO postgres;

--
-- TOC entry 163 (class 1259 OID 246970)
-- Dependencies: 5 164
-- Name: documento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE documento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.documento_id_seq OWNER TO postgres;

--
-- TOC entry 1943 (class 0 OID 0)
-- Dependencies: 163
-- Name: documento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE documento_id_seq OWNED BY documento.id;


--
-- TOC entry 165 (class 1259 OID 247053)
-- Dependencies: 5
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    nombre_usuario character varying(50) NOT NULL,
    clave_usuario character varying(250) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 162 (class 1259 OID 246959)
-- Dependencies: 5
-- Name: vocabulario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE vocabulario (
    id_palabra integer NOT NULL,
    palabra text
);


ALTER TABLE public.vocabulario OWNER TO postgres;

--
-- TOC entry 161 (class 1259 OID 246957)
-- Dependencies: 5 162
-- Name: vocabulario_id_palabra_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE vocabulario_id_palabra_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vocabulario_id_palabra_seq OWNER TO postgres;

--
-- TOC entry 1944 (class 0 OID 0)
-- Dependencies: 161
-- Name: vocabulario_id_palabra_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE vocabulario_id_palabra_seq OWNED BY vocabulario.id_palabra;


--
-- TOC entry 1824 (class 2604 OID 246975)
-- Dependencies: 164 163 164
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY documento ALTER COLUMN id SET DEFAULT nextval('documento_id_seq'::regclass);


--
-- TOC entry 1823 (class 2604 OID 246962)
-- Dependencies: 161 162 162
-- Name: id_palabra; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vocabulario ALTER COLUMN id_palabra SET DEFAULT nextval('vocabulario_id_palabra_seq'::regclass);


--
-- TOC entry 1830 (class 2606 OID 246981)
-- Dependencies: 164 164 1935
-- Name: pk_documento; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT pk_documento PRIMARY KEY (id);


--
-- TOC entry 1832 (class 2606 OID 247057)
-- Dependencies: 165 165 1935
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (nombre_usuario);


--
-- TOC entry 1826 (class 2606 OID 246969)
-- Dependencies: 162 162 1935
-- Name: vocabulario_palabra_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vocabulario
    ADD CONSTRAINT vocabulario_palabra_key UNIQUE (palabra);


--
-- TOC entry 1828 (class 2606 OID 246967)
-- Dependencies: 162 162 1935
-- Name: vocabulario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vocabulario
    ADD CONSTRAINT vocabulario_pkey PRIMARY KEY (id_palabra);


--
-- TOC entry 1940 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-02-12 12:34:25 COT

--
-- PostgreSQL database dump complete
--

